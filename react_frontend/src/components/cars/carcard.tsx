import React, { useEffect, useState } from "react";
import "./carcard.css";
import {RiDeleteBinLine} from "react-icons/ri";
import { Link } from 'react-router-dom';
import { Driver } from '../../types/types';

interface IOfferCardProps {
    id: number;
    components: number[];
    drivers: number[];
    maindriver: number;
    fetchCars: () => void;
}

const CarCard: React.FC<IOfferCardProps> = (props) => {

  const [driver, setDriver] = useState(null);
  var mainDriverMessage: string = "No main driver";

  const handleDeleteCar = () => {
    fetch('http://localhost:8082/car/?carId=' + props.id, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
      .then(response => {
        if (response.ok) {
          props.fetchCars();
        }
        else {
          throw new Error('Something went wrong while removing the car.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  }

  useEffect(() => {
    fetch(`http://localhost:8083/driver/get/id=${props.maindriver}`)
      .then(response => response.json())
      .then(data => setDriver(data));
  }, []);

  if (driver) {
    const d: Driver = driver;
    if (d.name) {
      mainDriverMessage = "Main driver: " + d.name + " " + d.surname;
    } else if (props.maindriver !== null) {
      mainDriverMessage = "Unknown main driver";
    }
  }
  
  return (
    <div className="car-card__card">
      <Link to={`/car/${props.id}`} key={props.id} className='car-card__title link-decorator'>
        <span className="car-card__text-format">Ferrari SF{props.id}</span>
      </Link>
      <span className="car-card__text-format car-card__nationality">
        <div>{props.components.length} components, {props.drivers.length} drivers</div>
        <div>{mainDriverMessage}</div>
      </span>
      <button className='car-card__delete-button' onClick={handleDeleteCar}>
        <RiDeleteBinLine className="car-card__delete-button__inner"/>
      </button>
    </div>
  );
};

export default CarCard;
