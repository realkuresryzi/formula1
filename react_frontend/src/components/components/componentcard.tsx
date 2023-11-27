import React from "react";
import "./componentcard.css";
import {RiDeleteBinLine} from "react-icons/ri";

interface IOfferCardProps {
    id: number;
    weight: number;
    price: number;
    manufacturer: string;
    name: string;
    fetchComponents: () => void;
}

const ComponentCard: React.FC<IOfferCardProps> = (props) => {

  const handleDeleteComponent = () => {
    fetch('http://localhost:8084/component/?componentId=' + props.id, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
      .then(response => {
        if (response.ok) {
          props.fetchComponents();
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
    <div className="component-card__card">
      <span className="component-card__text-format component-card__title">{props.name}</span>
      <span className="component-card__text-format component-card__nationality">
        <div>${props.price}, {props.weight} kg</div>
        <div>Made by {props.manufacturer}</div>
      </span>
      <button className='component-card__delete-button' onClick={handleDeleteComponent}>
        <RiDeleteBinLine className="component-card__delete-button__inner"/>
      </button>
    </div>
  );
};

export default ComponentCard;
