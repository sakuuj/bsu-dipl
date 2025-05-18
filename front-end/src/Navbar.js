import React from "react";
import logo from "./images/logoBSU.jpg"
import { Link } from "react-router-dom";

const JWT_TOKEN_KEY = "jwtToken";

export default function Navbar() {
    return (
        <nav>
            <ul className="list-none overflow-hidden bg-_dark-blue border justify-end border-_dark-blue flex">
                <li className="mr-auto ml-14">
                    <a
                        className="block text-white text-center hover:bg-_smoke hover:text-_dark-blue font-bold last:me-3"
                        href="https://bsu.by" target="_blank"
                    >
                        <img width={200} src={logo}></img>
                    </a>
                </li>
                {localStorage.getItem(JWT_TOKEN_KEY) === null
                    ?
                    <Link to={`/login`}
                        className="flex justify-center items-center text-white text-center hover:bg-_smoke hover:text-_dark-blue me-10 px-5"
                    >
                        <li className="text-xl">
                            Войти
                        </li>
                    </Link>
                    :
                    <Link to={`/login`} onClick={() => { localStorage.removeItem("jwtToken") }}
                        className="flex justify-center items-center text-white text-center hover:bg-_smoke hover:text-_dark-blue  me-10 px-5"
                    >
                        <li className="text-xl">
                            Выйти
                        </li>
                    </Link>
                }
            </ul>
        </nav>
    );
}