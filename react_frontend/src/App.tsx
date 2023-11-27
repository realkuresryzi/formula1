import React from "react";
import './App.css';
import { BrowserRouter, Route, Routes } from "react-router-dom";

import SideBar from "./components/sidebar/sidebar";
import Drivers from "./components/drivers/drivers";
import CarComponent from "./components/components/components";
import Cars from "./components/cars/cars";
import Races from "./components/races/races";
import Driver from "./components/drivers/driverdetail";
import Car from "./components/cars/cardetail";
import Home from "./components/home/home";


function App() {
  
  return (
    <BrowserRouter>
      <div className="App">
        <Routes>
          <Route path="/drivers" element={<div className="app-padding"><Drivers/></div>} />
          <Route path="/driver/:id" element={<div className="app-padding"><Driver/></div>} />
          <Route path="/cars" element={<div className="app-padding"><Cars/></div>} />
          <Route path="/car/:id" element={<div className="app-padding"><Car/></div>} />
          <Route path="/components" element={<div className="app-padding"><CarComponent/></div>} />
          <Route path="/races" element={<div className="app-padding"><Races/></div>} />
          <Route path="/" element={<div className="app-padding"><Home/></div>} />
        </Routes>
        <SideBar/>
      </div>
    </BrowserRouter>
  );
}

export default App;
