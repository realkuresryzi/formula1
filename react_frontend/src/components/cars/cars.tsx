import React, { useState, useEffect } from 'react';
import { Car } from './../../types/types';
import CarCard from './carcard';
import "./cars.css";

function CarList() {
  const [cars, setCars] = useState<Car[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error>();

  useEffect(() => {
    fetch('http://localhost:8082/car/all')
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Something went wrong while fetching the data.');
      })
      .then(data => {
        setCars(data);
        setLoading(false);
      })
      .catch(error => {
        setError(error);
        setLoading(false);
      });
  }, []);

  const handleAddCarsubmit = () => {
    fetch('http://localhost:8082/car/', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        componentIds: [],
        driverIds: [],
        mainDriverId: null
      })
    })
      .then(response => {
        if (response.ok) {
          fetchCars();
        }
        else {
          throw new Error('Something went wrong while adding the Car.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  }

  const fetchCars = () => {
    fetch('http://localhost:8082/car/all')
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Something went wrong while fetching the data.');
      })
      .then(data => {
        setCars(data);
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

  fetchCars();

  return (
    <div>
      <button className='cars__add-button' onClick={handleAddCarsubmit}>+</button>

      <div className="cars__cards-list__small-padding">
        {cars.map(c => (
          <div className="cars__card-area">
            <CarCard
              id={c.id}
              components={c.componentIdsNames}
              drivers={c.driverIdsNames}
              maindriver={c.mainDriverId}
              fetchCars={fetchCars}
            />
          </div>
        ))}
      </div>
    </div>
  )
}

export default CarList;