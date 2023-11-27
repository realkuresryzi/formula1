import React from "react";
import "./drivercard.css";
import {RiDeleteBinLine} from "react-icons/ri";
import { Link } from 'react-router-dom';

interface IOfferCardProps {
    id: number;
    name: string;
    surname: string;
    nationality: string;
    characteristics: {
      Experience: number;
      Consistency: number;
      Racecraft: number;
      Aggressiveness: number;
    };
    fetchDrivers: () => void;
}

const DriverCard: React.FC<IOfferCardProps> = (props) => {

  const handleDeleteDriver = () => {
    fetch('http://localhost:8083/driver/remove/id=' + props.id, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
      .then(response => {
        if (response.ok) {
          props.fetchDrivers();
        }
        else {
          throw new Error('Something went wrong while removing the driver.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  }

  return (
    <div className="driver-card__card">
      <Link to={`/driver/${props.id}`} key={props.id} className='driver-card__title link-decorator'>
        <span className="driver-card__text-format">{props.name} {props.surname}</span>
      </Link>
      <span className="driver-card__text-format driver-card__nationality">{props.nationality}</span>
      <button className='driver-card__delete-button' onClick={handleDeleteDriver}>
        <RiDeleteBinLine className="driver-card__delete-button__inner"/>
      </button>
    </div>
  );
};

export default DriverCard;
