import React, { useState, useEffect } from 'react';
import { Driver } from '../../types/types';
import "./assigned.css";
import { TbHelmet } from "react-icons/tb";

interface IDriversProps {
  driverIds: number[];
}

const AssignedDrivers: React.FC<IDriversProps> = (props) => {
  const { driverIds } = props;

  const [drivers, setDrivers] = useState<Driver[]>([]);

  useEffect(() => {
    const fetchDrivers = async () => {
      try {
        const promises = driverIds.map(id => fetch(`http://localhost:8083/driver/get/id=${id}`).then(res => res.json()));
        const driversData = await Promise.all(promises);
        setDrivers(driversData);
      } catch (error) {
        console.error("Drivers were not retrieved:(", error);
      }
    };
    fetchDrivers();
  }, [driverIds]);

  return (
    <div>
      <div className='assigned__name'>
        <TbHelmet className='assigned__icon'/>
        <b>Assigned drivers</b>
      </div>
      {drivers.length === 0 ? (
        <div className='assigned__name'>
          No assigned drivers
        </div>
      ) : (
        drivers.map((driver) => (
          <div key={driver.id} className='assigned__name'>
            {driver.name} {driver.surname}
          </div>
        ))
      )}
    </div>
  )  
};

export default AssignedDrivers;
