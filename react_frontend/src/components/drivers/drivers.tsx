import React, { useState, useEffect } from 'react';
import { Driver } from '../../types/types';
import DriverCard from './drivercard';
import "./drivers.css";
import './../../utils/styles.css';

function DriverList() {
  const [drivers, setDrivers] = useState<Driver[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error>();
  const [showAddDriverDialog, setShowAddDriverDialog] = useState(false);
  const [newDriverName, setNewDriverName] = useState("");
  const [newDriverSurname, setNewDriverSurname] = useState("");
  const [newDriverNationality, setNewDriverNationality] = useState("");

  useEffect(() => {
    fetch('http://localhost:8083/driver/get/all')
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Something went wrong while fetching the data.');
      })
      .then(data => {
        setDrivers(data);
        setLoading(false);
      })
      .catch(error => {
        setError(error);
        setLoading(false);
      });
  }, []);

  const handleAddDriverClick = () => {
    setShowAddDriverDialog(!showAddDriverDialog);
  }

  const handleAddDriverSubmit = () => {
    fetch('http://localhost:8083/driver/add', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: newDriverName,
        surname: newDriverSurname,
        nationality: newDriverNationality
      })
    })
      .then(response => {
        if (response.ok) {
          setShowAddDriverDialog(false);
          setNewDriverName("");
          setNewDriverSurname("");
          setNewDriverNationality("");
          fetchDrivers();
        }
        else {
          throw new Error('Something went wrong while adding the driver.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  }

  const fetchDrivers = () => {
    fetch('http://localhost:8083/driver/get/all')
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Something went wrong while fetching the data.');
      })
      .then(data => {
        setDrivers(data);
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
      {!showAddDriverDialog && <button className='driver__add-button' onClick={handleAddDriverClick}>+</button>}
      {showAddDriverDialog && <button className='driver__add-button' onClick={handleAddDriverClick}>-</button>}

      {showAddDriverDialog && (
        <div className="driver__dialog">
          <input type="text" className='driver__name-input-field driver__input-field' placeholder='  Name' value={newDriverName} onChange={(e) => setNewDriverName(e.target.value)} />
          <input type="text" className='driver__surname-input-field driver__input-field' placeholder='  Surname' value={newDriverSurname} onChange={(e) => setNewDriverSurname(e.target.value)} />
          <input type="text" className='driver__nationality-input-field driver__input-field' placeholder='  Nationality' value={newDriverNationality} onChange={(e) => setNewDriverNationality(e.target.value)} />
          <button className='driver__submit-button driver__input-field' onClick={handleAddDriverSubmit}>Submit</button>
        </div>
      )}

      <div className={showAddDriverDialog ? "driver__cards-list__large-padding" : "driver__cards-list__small-padding"}>
        {drivers.map((driver, index) => (
          <div key={index} className="driver__card-area">
            <DriverCard
              id={driver.id}
              name={driver.name}
              surname={driver.surname}
              nationality={driver.nationality}
              characteristics={driver.characteristics}
              fetchDrivers={fetchDrivers}
            />
          </div>
        ))}
      </div>
    </div>
  )
}

export default DriverList;