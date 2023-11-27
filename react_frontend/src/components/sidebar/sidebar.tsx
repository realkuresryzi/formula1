import React from "react";
import './sidebar.css';
import logo from './../../images/logo.png';
import { Link, useLocation } from 'react-router-dom';

export const Sidebar = () =>  {

  const location = useLocation();

  return (
    <ul className="nav-bar-list">
      <li className="nav-bar-list__item">
        <Link to={'/'}>
          <img className="logo-size" src={logo} alt="Team logo"/>
        </Link>
      </li>
      <li className="nav-bar-list__item nav-bar-list__item--menu">
        <Link to='/races' className={location.pathname === "/races" ? "link-text current-link" : "link-text"}>
          Races
        </Link>
      </li>
      <li className="nav-bar-list__item nav-bar-list__item--menu">
        <Link to='/seasons' className={location.pathname === "/seasons" ? "link-text current-link" : "link-text"}>
          Seasons
        </Link>
      </li>
      <li className="nav-bar-list__item nav-bar-list__item--menu">
        <Link to='/drivers' className={location.pathname === "/drivers" ? "link-text current-link" : "link-text"}>
          Drivers
        </Link>
      </li>
      <li className="nav-bar-list__item nav-bar-list__item--menu">
        <Link to='/cars' className={location.pathname === "/cars" ? "link-text current-link" : "link-text"}>
          Cars
        </Link>
      </li>
      <li className="nav-bar-list__item nav-bar-list__item--menu">
        <Link to='/components' className={location.pathname === "/components" ? "link-text current-link" : "link-text"}>
          Components
        </Link>
      </li>
    </ul>
  );
}

export default Sidebar;