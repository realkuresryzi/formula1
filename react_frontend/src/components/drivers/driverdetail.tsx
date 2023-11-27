import React, { useState, useEffect } from 'react';
import { useParams, Link } from "react-router-dom";
import { Driver } from '../../types/types';
import "./driverdetail.css";
import { TbPointFilled, TbPoint } from "react-icons/tb";
import { AiOutlineEdit } from "react-icons/ai";
import { RiDeleteBinLine } from "react-icons/ri";
import { FiSave } from "react-icons/fi";

function DriverDetail() {
  const [driver, setDriver] = useState(null);
  const { id } = useParams();
  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [nationality, setNationality] = useState("");  

  const [editMode, setEditMode] = useState(false);
  const handleEditDriver = async () => {
    if (editMode) {
      try {
        const response = await fetch(`http://localhost:8083/driver/update/id=${id}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name,
            surname,
            nationality,
          }),
        });
        const data = await response.json();
        console.log(data);
      } catch (error) {
        console.error(error);
      }
    }
    setEditMode(!editMode);
  }

  const handleNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setName(event.target.value);
  }
  const handleSurnameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSurname(event.target.value);
  }
  const handleNationalityChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setNationality(event.target.value);
  }

  useEffect(() => {
    fetch(`http://localhost:8083/driver/get/id=${id}`)
      .then(response => response.json())
      .then(data => setDriver(data))
      .catch(error => console.error(error));
  }, [id]);
  

  if (!driver) {
    return <div>Loading...</div>;
  }

  const handleDeleteDriver = () => {
    fetch('http://localhost:8083/driver/remove/id=' + id, {
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

  const d: Driver = driver;
  var agrArray = Array(d.characteristics.Aggressiveness).fill(null);
  var agrInvArray = Array(5 - d.characteristics.Aggressiveness).fill(null);
  var conArray = Array(d.characteristics.Consistency).fill(null);
  var conInvArray = Array(5 - d.characteristics.Consistency).fill(null);
  var expArray = Array(d.characteristics.Experience).fill(null);
  var expInvArray = Array(5 - d.characteristics.Experience).fill(null);
  var racArray = Array(d.characteristics.Racecraft).fill(null);
  var racInvArray = Array(5 - d.characteristics.Racecraft).fill(null);

  return (
    <div className='dd__container'>
      <div className='dd__upper-block'>
        <img className='dd__pic' src="https://media.formula1.com/content/dam/fom-website/drivers/2023Drivers/leclerc.jpg.img.640.medium.jpg/1677069223130.jpg" alt="Random driver" />
        <div className='dd__text'>
          <div className={editMode ? 'hidden' : 'dd__name'}>{d.name}</div>
          <div className={editMode ? 'hidden' : 'dd__surname'}>{d.surname}</div>
          <div className={editMode ? 'hidden' : 'dd__nat'}>Nationality: {d.nationality}</div>

          <div className={!editMode ? 'hidden' : 'dd__name'}>
            <input type="text" defaultValue={d.name} onChange={handleNameChange}/>
          </div>
          <div className={!editMode ? 'hidden' : 'dd__surname'}>
            <input type="text" defaultValue={d.surname} onChange={handleSurnameChange}/>
          </div>
          <div className={!editMode ? 'hidden' : 'dd__nat'}>
            Nationality: 
            <input type="text" defaultValue={d.nationality} onChange={handleNationalityChange}/>
          </div>
        </div>
      </div>

      <div className='dd__lower-block'>
        <div className='dd__characteristcs'>
          <h3>Characteristics:</h3>

          <div className='dd__characteristic'>
            <span className='dd__char-name'>Aggresivness: </span>
            {agrArray.map((_, __) => (
              <TbPointFilled className='dd__char-point'/>
            ))}
            {agrInvArray.map((_, __) => (
              <TbPoint className='dd__char-point' />
            ))}
          </div>

          <div className='dd__characteristic'>
            <span className='dd__char-name'>Consistency: </span>
              {conArray.map((_, __) => (
                <TbPointFilled className='dd__char-point' />
              ))}
              {conInvArray.map((_, __) => (
                <TbPoint className='dd__char-point' />
              ))}
          </div>

          <div className='dd__characteristic'>
            <span className='dd__char-name'>Experience: </span>
              {expArray.map((_, __) => (
                <TbPointFilled className='dd__char-point' />
              ))}
              {expInvArray.map((_, __) => (
                <TbPoint className='dd__char-point' />
              ))}
          </div>

          <div className='dd__characteristic'>
            <span className='dd__char-name'>Racecraft: </span>
              {racArray.map((_, __) => (
                <TbPointFilled className='dd__char-point' />
              ))}
              {racInvArray.map((_, __) => (
                <TbPoint className='dd__char-point' />
              ))}
          </div>
        </div>

        <div className='dd__buttons'>
          <button className='dd__button dd__outer-edit' onClick={handleEditDriver}>
            <AiOutlineEdit className={editMode ? 'hidden' : 'dd__button-size dd__edit-button'}/>
            <FiSave className={editMode ? 'dd__button-size dd__edit-button' : 'hidden'}/>
          </button>
          <Link to={'/drivers'}>
            <button className='dd__button dd__outer-delete' onClick={handleDeleteDriver}>
              <RiDeleteBinLine className='dd__button-size dd__delete-button'/>
            </button>
          </Link>
        </div>
      </div>

    </div>
  );
}

export default DriverDetail;
