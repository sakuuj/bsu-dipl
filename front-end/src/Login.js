import React from "react";
import Cookies from "js-cookie";
import { useState } from "react";
import { jwtDecode } from 'jwt-decode';
import { Link } from "react-router-dom";

const JWT_COOKIE_NAME = "X-JWT-TOKEN";


export default function LoginForm({ authed, setAuthed, setRole }) {

  console.log(setRole);
  let [errorMsg, setErrorMsg] = useState("");
  let [userName, setUserName] = useState("");
  let [password, setPassword] = useState("");

  return (
    <div className="w-4/5 flex justify-center items-center  bg-_dark-blue h-dvh">
      <div className="flex-col justify-center items-center p-5 bg-_dark-blue text-white m-2">
        <div className="text-center font-bold text-2xl border-b border-_grayer-white pb-2">ВХОД</div>
        <div className="grow-0">
          <div className=" border border-_grayer-white flex justify-between items-center mb-5 mt-10">
            <label className="mx-auto px-5">Имя</label>
            <input className=" border border-_dark-blue ml-2 text-_dark-blue" type="text" value={userName} onChange={(e) => setUserName(e.target.value)}></input>
          </div>
          <div className="border  border-_grayer-white flex justify-between items-center mb-2">
            <label className="mx-auto px-5">Пароль</label>
            <input className="border border-_dark-blue ml-2 text-_dark-blue" type="password" value={password} onChange={(e) => setPassword(e.target.value)}></input>
          </div>
        </div>
     
        <div className="flex justify-end mb-10">
          <Link to={`/register`} className="hover:text-_smoke">Создать аккаунт</Link>
        </div>
        {errorMsg !== "" ? (
        <div className="p-4 overflow-hidden">
          <label className="bg-white text-_dark-blue p-1">{errorMsg}</label>
        </div>) : null
        }
        <div className="flex justify-center items-center">
          <div onClick={() => {
            const xhr = new XMLHttpRequest();
            xhr.open("POST", "http://localhost:8080/login");
            let params = `password=${password}&userName=${userName}`;
            console.log(params);
            xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

            xhr.onerror = function() {
              setErrorMsg("Произошла ошибка при подключении к серверу, попробуйте войти позже")
            }
            xhr.onload = function () {
              let response = xhr.response;

              if (xhr.status !== 200) {
                setErrorMsg("Неправильное имя или пароль");
                return;
              }

              let jwtToken = response;
              console.log(jwtToken);
              Cookies.set(JWT_COOKIE_NAME, jwtToken);
              let decoded = jwtDecode(jwtToken);
              console.log(decoded);
              setRole(new String(decoded.role));
              setAuthed(true);
            };
            xhr.send(params);
          }}
            className="text-center py-4 px-14 hover:bg-_dark-blue hover:text-_grayer-white hover:border hover:border-_grayer-white bg-white text-_dark-blue hover:cursor-pointer "
          >
            Войти
          </div>
        </div>
      </div>
    </div>
  );
}