import React from "react";
import Cookies from "js-cookie";
import { useState } from "react";
import { jwtDecode } from 'jwt-decode';



import Navbar from "./Navbar";
import Main from "./Main";
import LoginForm from "./Login";

const JWT_COOKIE_NAME = "X-JWT-TOKEN";


function App() {


  let [authed, setAuthed] = useState(false);
  let [role, setRole] = useState(null);

  if (!authed) {
    return <LoginForm authed={authed} setAuthed={setAuthed} setRole={setRole} />
  }
  console.log(role);

  return (
    <div className="h-full min-w-full">
      <Navbar />
      <Main role={role} />
    </div>
  );
}

export default App;
