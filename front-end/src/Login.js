import React from "react";
import { useState } from "react";
import { Link } from "react-router-dom";



export default function LoginForm() {

  let [errorMsg, setErrorMsg] = useState("");
  let [userName, setUserName] = useState("");
  let [password, setPassword] = useState("");

  return (
    <div className="w-full flex justify-center items-center  bg-_dark-blue h-dvh">
      <div className="flex-col justify-center items-center p-5 bg-_dark-blue text-white m-2">
        <div className="text-center font-bold text-2xl border-b border-_grayer-white pb-2">ВХОД</div>
        <div className="grow-0">
          <div className=" border border-_grayer-white flex justify-between  items-center mb-5 mt-10">
            <label className="flex-1 px-5 text-center hover:bg-white hover:text-_dark-blue" for="login">Логин</label>
            <input  id="login" className="border  border-_dark-blue px-2 text-_dark-blue" type="text" value={userName} onChange={(e) => setUserName(e.target.value)}></input>
          </div>
          <div className="border  border-_grayer-white flex justify-between items-center mb-2">
            <label for="passwd" className="flex-1 text-center mx-auto hover:bg-white hover:text-_dark-blue">Пароль</label>
            <input id="passwd" className="border border-_dark-blue px-2 text-_dark-blue" type="password" value={password} onChange={(e) => setPassword(e.target.value)}></input>
          </div>
        </div>

        {<div className="flex justify-end mb-5">
          <Link to={`/`} onClick={() => {localStorage.removeItem("jwtToken")}} className="hover:text-_smoke">На главную</Link>
        </div>}
        {errorMsg !== "" ? (
          <>
            <label className="bg-white text-center text-_dark-blue p-1 absolute bottom top-20 left-1/2 -translate-x-1/2 -translate-y-3/4">{errorMsg}</label>
          </>) : null
        }
        <div className="flex justify-center items-center">
          <div onClick={() => {
            const xhr = new XMLHttpRequest();
            xhr.open("POST", "http://localhost:8080/login");
            let params = `password=${password}&userName=${userName}`;
            console.log(params);
            xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');

            xhr.onerror = function () {
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

              localStorage.setItem("jwtToken", jwtToken);
            };
            xhr.send(params);

            localStorage.removeItem("jwtToken")
          }}
            className="text-center py-2 px-14 hover:bg-_dark-blue hover:text-_grayer-white border hover:border-_grayer-white bg-white text-_dark-blue hover:cursor-pointer "
          >
            Войти
          </div>
        </div>
      </div>
    </div>
  );
}