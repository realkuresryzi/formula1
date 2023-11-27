import React, { useState, useEffect } from 'react';
import { Component } from '../../types/types';
import './assigned.css';
import { TbEngine } from 'react-icons/tb';

interface IComponentsProps {
  componentIds: number[];
}

const AssignedComponents: React.FC<IComponentsProps> = (props) => {
  const { componentIds } = props;

  const [components, setComponents] = useState<Component[]>([]);

  useEffect(() => {
    function getComponents() {
      const componentPromises = componentIds.map((id) => {
        console.log('fetching component with id ' + id);
        return fetch(`http://localhost:8084/component/id?componentId=${id}`)
          .then((response) => response.json())
          .catch((error) => {
            console.error('Component was not retrieved :(', error);
            return null;
          });
      });
      Promise.all(componentPromises).then((componentData) => {
        const filteredComponents = componentData.filter(
          (component) => component !== null
        );
        setComponents(filteredComponents);
      });
    }

    getComponents();
  }, [componentIds]);

  console.log(components);

  return (
    <div>
      <div className="assigned__name">
        <TbEngine className="assigned__icon" />
        <b>Added components</b>
      </div>
      {components.length === 0 ? (
        <div className="assigned__name">No added components</div>
      ) : (
        components.map((component) => (
          <div key={component.id} className="assigned__name">
            {component.name}
          </div>
        ))
      )}
    </div>
  );
};

export default AssignedComponents;
