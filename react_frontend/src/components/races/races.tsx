import React, { useState, useEffect } from 'react';
import { Race } from './../../types/types';
import { RiDeleteBinLine } from "react-icons/ri";
import "./races.css";

function RaceList() {
  const [races, setRaces] = useState<Race[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<Error>();
  const [showAddRaceDialog, setShowAddRaceDialog] = useState(false);
  const [newCarName, setNewCarName] = useState("");
  const [newCarsurname, setNewCarsurname] = useState("");
  const [newCarNationality, setNewCarNationality] = useState("");

  useEffect(() => {
    fetch('http://localhost:8081/race/')
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Something went wrong while fetching the data.');
      })
      .then(data => {
        setRaces(data);
        setLoading(false);
      })
      .catch(error => {
        setError(error);
        setLoading(false);
      });
  }, []);

  const handleAddRaceClick = () => {
    setShowAddRaceDialog(!showAddRaceDialog);
  }

  const handleAddRacesubmit = () => {
    fetch('http://localhost:8083/Car/add', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: newCarName,
        surname: newCarsurname,
        nationality: newCarNationality
      })
    })
      .then(response => {
        if (response.ok) {
          setShowAddRaceDialog(false);
          setNewCarName("");
          setNewCarsurname("");
          setNewCarNationality("");
          fetchRaces();
        }
        else {
          throw new Error('Something went wrong while adding the Car.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  }

  function handleDeleteRace(id: number) {
    fetch('http://localhost:8081/race/?raceId=' + id, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    })
    .then(response => {
      if (response.ok) {
      }
      else {
        throw new Error('Something went wrong while removing the driver.');
      }
    })
    .catch(error => {
      console.error(error);
    });
  }

  const fetchRaces = () => {
    fetch('http://localhost:8081/race/')
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Something went wrong while fetching the data.');
      })
      .then(data => {
        setRaces(data);
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
    <div className='races__container'>
      {races.map(race => (
        <div className="races__card">
          <div className='races__title'>
            <span className="races__text-format">{race.raceInfo.name}</span>
          </div>
          <div className="races__location">
            <span className='races__text-format'>Location: {race.raceInfo.location}</span>
            <span className="races__text-format">Prizepool: ${race.raceInfo.prizePool}</span>
          </div>
          <button className='races__delete-button' onClick={() => handleDeleteRace(race.id)}>
            <RiDeleteBinLine className="races__delete-button__inner"/>
          </button>
        </div>
      ))}
    </div>
  )
}

export default RaceList;