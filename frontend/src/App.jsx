import { Button } from "@mui/material"
import { useContext, useEffect, useState } from "react"
import { AuthContext } from "react-oauth2-code-pkce"
import { useDispatch } from "react-redux"
import { BrowserRouter as Router } from "react-router"
import { setCredentials } from "./store/authSlice"

const App = () => {
  const { token, tokenData, logIn, logOut, isAuthenticated } = useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    if (token && tokenData) {
      dispatch(setCredentials({ token, user: tokenData }));
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch]);

  const handleLogin = () => {
    logIn();
  };

  const handleLogout = () => {
    logOut();
  };

  return (
    <Router>
      <div style={{ padding: "20px" }}>
        <h1>FitOps App</h1>

        {!token ? (
          !isAuthenticated ? (
            <Button
              variant="contained"
              color="primary"
              onClick={handleLogin}
            >
              LOGIN
            </Button>
          ) : (
            <div>
              <pre>{JSON.stringify(tokenData, null, 2)}</pre>
            </div>
          )
        ) : (
          <div>
            <p>Welcome, {tokenData?.name || tokenData?.preferred_username}!</p>
            <Button
              variant="outlined"
              color="secondary"
              onClick={handleLogout}
            >
              LOGOUT
            </Button>
          </div>
        )}
      </div>
    </Router>
  );
};

export default App;