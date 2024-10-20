import React from "react";
import { useState } from "react";
import { Link } from "react-router-dom";


export default function RegistrationForm() {

  let [responseMsg, setResponseMsg] = useState("");
  let [userName, setUserName] = useState("");
  let [password, setPassword] = useState("");

  return (
    <div className="w-4/5 flex justify-center items-center  bg-_dark-blue h-full">
      <div className="flex-col  justify-center items-center p-5 bg-_dark-blue text-white m-2">
        <div className="text-center font-bold text-2xl border-b border-_grayer-white pb-2">РЕГИСТРАЦИЯ</div>
        <div className="">
          <div className=" border border-_grayer-white flex justify-between items-center mb-5 mt-10">
            <label className="mx-auto px-5">Имя</label>
            <input className=" border border-_dark-blue ml-2 text-_dark-blue" type="text" value={userName} onChange={(e) => setUserName(e.target.value)}></input>
          </div>
          <div className="border  border-_grayer-white flex justify-between items-center ">
            <label className="mx-auto px-5">Пароль</label>
            <input className="border border-_dark-blue ml-2 text-_dark-blue" type="password" value={password} onChange={(e) => setPassword(e.target.value)}></input>
          </div>
        </div>
             
        <div className="flex justify-end mb-10">
          <Link to={`/`} className="hover:text-_smoke">На главную</Link>
        </div>
        {responseMsg !== "" ? (
        <div className="p-4 overflow-hidden flex justify-center items-center">
          <label className="bg-white text-_dark-blue p-1">{responseMsg}</label>
        </div>) : null
        }
        <div className="flex justify-center items-center">
          <div
            className="text-center py-4 px-8 hover:bg-_dark-blue hover:text-_grayer-white hover:border hover:border-_grayer-white bg-white text-_dark-blue hover:cursor-pointer "
            onClick={() => {
              let userReq = {
                userName: userName,
                rawPassword: password
              }
              let userReqJSON = JSON.stringify(userReq);

              let xhr = new XMLHttpRequest();
              xhr.open("POST", "http://localhost:8080/register");

              console.log(userReqJSON);
              xhr.setRequestHeader('Content-type', 'application/json');

              xhr.onerror = function () {
                setResponseMsg("Произошла ошибка при подключении к серверу, попробуйте войти позже")
              }
              xhr.onload = function () {
                let response = xhr.response;

                if (xhr.status !== 201) {
                  setResponseMsg(response);
                  return;
                }

                setResponseMsg("Вы были зарегистрированы")
              };
              xhr.send(userReqJSON);

            }}
          >
            Зарегистрироваться
          </div>
        </div>
      </div>
    </div>
  );
}