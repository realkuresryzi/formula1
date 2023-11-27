import React, { useState, useEffect } from 'react';
import { useParams, Link } from "react-router-dom";
import { Car, Driver } from '../../types/types';
import "./cardetail.css";
import { AiOutlineEdit } from "react-icons/ai";
import { RiDeleteBinLine } from "react-icons/ri";
import AssignedDrivers from "./assigneddrivers";
import AssignedComponents from "./assignedcomponents";

function CarDetail() {
  const [car, setCar] = useState<Car | null>(null);
  const [mainDriver, setMainDriver] = useState(null);
  const { id } = useParams();

  useEffect(() => {
    fetch(`http://localhost:8082/car/?carId=${id}`)
      .then(response => response.json())
      .then(data => {
        setCar(data);
        return fetch(`http://localhost:8083/driver/get/id=${data.mainDriverId}`)
          .then(response => response.json())
          .then(data => setMainDriver(data))
          .catch(error => console.error(error));
      })
      .catch(error => console.error(error));
  }, [id]);

  const handleDeleteCar = () => {
    fetch(`http://localhost:8082/car/?carId=${id}`, {
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
          throw new Error('Something went wrong while removing the car.');
        }
      })
      .catch(error => {
        console.error(error);
      });
  }

  const handleEditCar = () => {
  }

  if (!car) {
    return <div>Loading...</div>;
  }
  const c: Car = car;

  let mainDriverMessage: string = "No main driver";
  if (mainDriver) {
    const d: Driver = mainDriver;
    console.log(d);
    if (d.name) {
      mainDriverMessage = "Main driver: " + d.name + " " + d.surname;
    } else if (c.mainDriverId !== null) {
      mainDriverMessage = "Unknown main driver";
    }
  }

  return (
    <div className='cd__container'>
      <div className='cd__upper-block'>
        <div className='cd__name-main-driver'>
          <h1>Ferrari SF{c.id}</h1>
          <h3>{mainDriverMessage}</h3>
        </div>
        <div className='cd__buttons'>
          <Link to={'/'}>
            <button className='cd__button cd__outer-edit' onClick={handleEditCar}>
              <AiOutlineEdit className='cd__button-size cd__edit-button' />
            </button>
          </Link>
          <Link to={'/cars'}>
            <button className='cd__button cd__outer-delete' onClick={handleDeleteCar}>
              <RiDeleteBinLine className='cd__button-size cd__delete-button' />
            </button>
          </Link>
        </div>
      </div>

      <div className='cd__lower-block'>
        <div className='cd__drivers-block'>
          <AssignedDrivers
            driverIds={c.driverIdsNames}
          />
        </div>
        <div className='cd__components-block'>
          <AssignedComponents
            componentIds={c.componentIdsNames}
          />
        </div>
      </div>

    </div>
  );
}

export default CarDetail;
