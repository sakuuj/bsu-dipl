import React from "react";

export default function Navbar() {
    return (
        <nav>
            <ul className="list-none overflow-hidden bg-_dark-blue">
                <li className="float-right">
                    <a
                        className="block text-white text-center px-4 py-4 hover:bg-_smoke hover:text-_dark-blue font-bold last:me-3"
                        href="#"
                    >
                        Главная
                    </a>
                </li>
            </ul>
        </nav>
    );
}