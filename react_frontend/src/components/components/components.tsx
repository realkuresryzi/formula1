import React, { useState, useEffect } from 'react';
import { Component } from './../../types/types';
import ComponentCard from './componentcard';
import "./components.css";
import { Link } from 'react-router-dom';

function ComponentList() {
  const [components, setComponents] = useState<Component[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error>();
  const [showAddComponentDialog, setShowAddComponentDialog] = useState(false);
  const [newPrice, setNewPrice] = useState("");
  const [newManufacturer, setNewManufacturer] = useState("");
  const [newWeight, setNewWeight] = useState("");
  const [newName, setNewName] = useState("");

  useEffect(() => {
    fetch('http://localhost:8084/component/')
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Something went wrong while fetching the data.');
      })
      .then(data => {
        setComponents(data);
        setLoading(false);
      })
      .catch(error => {
        setError(error);
        setLoading(false);
      });
  }, []);

  const handleAddComponentClick = () => {
    setShowAddComponentDialog(!showAddComponentDialog);
  }

  const handleAddComponentSubmit = () => {
    fetch('http://localhost:8084/component/', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        weight: newWeight,
        price: newPrice,
        manufacturer: newManufacturer,
        name: newName
      })
    })
      .then(response => {
        if (response.ok) {
          setShowAddComponentDialog(false);
          setNewWeight("");
          setNewPrice("");
          setNewManufacturer("");
          setNewName("");
          fetchComponents();
        }
        else {
          throw new Error('Something went wrong while adding the component.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  }

  const fetchComponents = () => {
    fetch('http://localhost:8084/component/', {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        }
      })
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Something went wrong while fetching the data.');
      })
      .then(data => {
        setComponents(data);
      })
      .catch(error => {
        setError(error);
      });
  }

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error.message}</div>;
  }

  return (
    <div>
      <h2>Component List</h2>
      {!showAddComponentDialog && <button className='component__add-button' onClick={handleAddComponentClick}>+</button>}
      {showAddComponentDialog && <button className='component__add-button' onClick={handleAddComponentClick}>-</button>}

      {showAddComponentDialog && (
        <div className="component__dialog">
          <input type="component__text" className='component__name-input-field component__input-field' placeholder='  Name' value={newName} onChange={(e) => setNewName(e.target.value)} />
          <input type="component__text" className='component__weight-input-field component__input-field' placeholder='  Weight in kg' value={newWeight} onChange={(e) => setNewWeight(e.target.value)} />
          <input type="component__text" className='component__price-input-field component__input-field' placeholder='  Price in $' value={newPrice} onChange={(e) => setNewPrice(e.target.value)} />
          <input type="component__text" className='component__manu-input-field component__input-field' placeholder='  Manufacturer' value={newManufacturer} onChange={(e) => setNewManufacturer(e.target.value)} />
          <button className='component__submit-button component__input-field' onClick={handleAddComponentSubmit}>Submit</button>
        </div>
      )}

      <div className={showAddComponentDialog ? "component__cards-list__large-padding" : "component__cards-list__small-padding"}>
        {components.map(component => (
          <div className="component__card-area">
            <ComponentCard
              id={component.id}
              weight={component.weight}
              price={component.price}
              manufacturer={component.manufacturer}
              name={component.name}
              fetchComponents={fetchComponents}
            />
          </div>
        ))}
      </div>
    </div>
  )
}

export default ComponentList;