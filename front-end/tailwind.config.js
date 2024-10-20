/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        '_dark-blue': '#303b44',
        '_grayer-white': '#f0f0f0',
        '_smoke': '#d5d5d5'
      }
    },
  },
  plugins: [],
}

