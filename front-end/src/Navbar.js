import React from "react";
import logo from "./images/logoBSU.jpg"
import { Link } from "react-router-dom";
export default function Navbar() {
    return (
        <nav>
            <ul className="list-none overflow-hidden bg-_dark-blue border border-_dark-blue">
                <li className="float-right">
                    <a
                    className="block text-white text-center hover:bg-_smoke hover:text-_dark-blue font-bold last:me-3"
                        href="https://bsu.by" target="_blank"
                    >
                        <img width={200} src={logo}></img>
                    </a>
                </li>
            </ul>
        </nav>
    );
}