import React from "react";

import { jwtDecode } from 'jwt-decode';
import "core-js/stable/atob";
import { Navigate, useNavigate } from "react-router-dom";

import Navbar from "./Navbar";
import Main from "./Main";
import LoginForm from "./Login";



function App() {


  let navigate = useNavigate();
  let role = null;

  let shouldRedirectToLogin = false;

  let jwtToken = localStorage.getItem("jwtToken");

  if (jwtToken !== null && isExpired("" + jwtToken)) {
    return (<Navigate to={"/login"}></Navigate>)
  } else if (jwtToken !== null) {
    role = "ADMIN"
  }


  return shouldRedirectToLogin ? <LoginForm /> : (
    <div className="h-full min-w-full">
      <Navbar />
      <Main role={role} />
    </div>
  );
}

function isExpired(token) {

  let isExpired = false;
  console.log(token);
  let decodedToken = jwtDecode(token);
  console.log(decodedToken)
  let dateNow = new Date();
  console.log(dateNow)

  console.log(decodedToken.exp)
  console.log(dateNow.getTime())
  if (decodedToken.exp < dateNow.getTime() / 1000) {
    isExpired = true;
  }
  return isExpired;

}
export default App;
