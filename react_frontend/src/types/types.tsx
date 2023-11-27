export type Driver = {
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
  };

export type Car = {
  id: number;
  componentIdsNames: number[];
  driverIdsNames: number[];
  mainDriverId: number;
}

export type Component = {
  id: number;
  weight: number;
  price: number;
  manufacturer: string;
  name: string;
}

export type Race = {
  id: number;
  raceInfo: {
    location: string;
    name: string;
    prizePool: number;
  },
  driverOne: {
    driverId: number;
    carId: number;
    position: number;
  },
  driverTwo: {
    driverId: number;
    carId: number;
    position: number;
  }
}

export type Season = {
  id: number;
  year: number;
  races: [
    {
      id: number;
      name: string;
    }
  ]
}
