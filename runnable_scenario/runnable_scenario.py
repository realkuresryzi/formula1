# @author Andrej Šimurka
#
# In this runnable scenario, we simulate the scenario where the manager
# wants to create some initial entities of the F1 team, assign them to each
# other and finally performs a advanced functionality.
#
# In particular, the manager:
# - adds drivers, components, seasons, races and cars
# - assign races to seasons
# - assign drivers to cars
# - SET MAIN DRIVERS FOR CARS
# - assign drivers for particular races
# - assign drivers points they earned
# - computes the best resulting driver for each race location

# This scenario does not manipulate with car components, so we decided to omit them
# and use only fictive cars with no assigned components

from locust import SequentialTaskSet, task, constant, HttpUser
from locust.exception import StopUser
from json import dumps, loads
from random import sample

seasons_templates = [
    {'year': 2020,
     'races': []},
    {'year': 2021,
     'races': []},
    {'year': 2022,
     'races': []}
]

races_templates = [
    {'raceInfo': {
        'location': "SILVERSTONE",
        'name': "Great Britain Grand Prix 2020",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "SILVERSTONE",
        'name': "Great Britain Grand Prix 2021",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "SILVERSTONE",
        'name': "Great Britain Grand Prix 2022",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "MONACO",
        'name': "Monaco Grand Prix 2020",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "MONACO",
        'name': "Monaco Grand Prix 2021",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "MONACO",
        'name': "Monaco Grand Prix 2022",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "MONZA",
        'name': "Italian Grand Prix 2020",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "MONZA",
        'name': "Italian Grand Prix 2021",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "MONZA",
        'name': "Italian Grand Prix 2022",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "INTERLAGOS",
        'name': "Brazilian Grand Prix 2020",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "INTERLAGOS",
        'name': "Brazilian Grand Prix 2021",
        'prizePool': '999999'
    }},
    {'raceInfo': {
        'location': "INTERLAGOS",
        'name': "Brazilian Grand Prix 2022",
        'prizePool': '999999'
    }}
]

drivers_templates = [
    {'name': "Charles",
     'surname': "Leclerc",
     'nationality': "Monegasque",
     },
    {'name': "Carlos",
     'surname': "Sainz",
     'nationality': "Spanish",
     },
    {'name': "Antonio",
     'surname': "Giovinazzi",
     'nationality': "Italian",
     },
    {'name': "Robert",
     'surname': "Shwartzman",
     'nationality': "Israeli",
     }
]

cars_templates = [
    {'componentIds': [],
     'driverIds': []
     },
    {'componentIds': [],
     'driverIds': []
     }
]


class UserUseCase(SequentialTaskSet):

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.seasons = []
        self.races = []
        self.cars = []
        self.drivers = []
        self.locations = ["SILVERSTONE", "MONZA", "MONACO", "INTERLAGOS"]

    def on_start(self):
        """At the beginning, all local lists are cleared"""

        self.seasons = []
        self.races = []
        self.cars = []
        self.drivers = []

    @task
    def add_drivers(self):
        """Posts all drivers to the database"""

        for driver in drivers_templates:
            with self.client.post(url="http://localhost:8083/driver/add",
                                  headers={'content-type': 'application/json'},
                                  catch_response=True,
                                  data=dumps(driver)) as response:

                if response.status_code == 200:
                    response.success()
                    self.drivers.append(loads(response.content))
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def add_season(self):
        """Posts all seasons to the database"""

        for season in seasons_templates:
            with self.client.post(url="http://localhost:8081/season/",
                                  headers={'content-type': 'application/json'},
                                  catch_response=True,
                                  data=dumps(season)) as response:

                if response.status_code == 200:
                    response.success()
                    self.seasons.append(loads(response.content))
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def add_races(self):
        """Posts all races to the database"""

        for race in races_templates:
            with self.client.post(url="http://localhost:8081/race/",
                                  headers={'content-type': 'application/json'},
                                  catch_response=True,
                                  data=dumps(race)) as response:

                if response.status_code == 200:
                    response.success()
                    self.races.append(loads(response.content))
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def add_cars(self):
        """Posts all cars to the database"""
        for car in cars_templates:
            with self.client.post(url="http://localhost:8082/car/",
                                  headers={'content-type': 'application/json'},
                                  catch_response=True,
                                  data=dumps(car)) as response:

                if response.status_code == 200:
                    response.success()
                    self.cars.append(loads(response.content))
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def assign_races_to_season(self):
        """Assigns previously posted races to previously posted seasons, such that each
        season has exactly 4 races with same quadruple of locations"""

        for i in range(len(self.races)):
            with self.client.patch(url="http://localhost:8081/season/addRace?seasonId={}&raceId={}"
                    .format(self.seasons[i % 3]['id'], self.races[i]['id']),
                                   headers={'content-type': 'application/json'},
                                   catch_response=True) as response:
                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def assign_drivers_to_cars(self):
        """Assigns previously posted drivers to previously posted cars, such that each car has 2 drivers"""

        for i in range(len(self.drivers)):
            with self.client.put(url="http://localhost:8082/cardriver/assign?driverId={}&carId={}"
                    .format(self.drivers[i]['id'], self.cars[i % 2]['id']),
                                 headers={'content-type': 'application/json'},
                                 catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def assign_main_drivers(self):
        """Assigns main drivers for each of two previously posted cars"""

        for i in range(2):
            with self.client.put(url="http://localhost:8082/cardriver/setmain?carId={}&driverId={}"
                    .format(self.cars[i]['id'], self.drivers[i]['id']),
                                 headers={'content-type': 'application/json'},
                                 catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def assign_drivers_to_races(self):
        """Assigns previously posted drivers to previously posted races in their corresponding
        cars, such that main drivers Leclerc and Sainz are assigned to all the races"""

        for i in range(len(self.races)):

            with self.client.patch(
                    url="http://localhost:8081/race/assignDriverOne?driverOneId={}&raceId={}&carId={}"
                            .format(self.drivers[0]['id'], self.races[i]['id'], self.cars[0]['id']),
                    headers={'content-type': 'application/json'},
                    catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

            with self.client.patch(
                    url="http://localhost:8081/race/assignDriverTwo?driverTwoId={}&raceId={}&carId={}"
                            .format(self.drivers[1]['id'], self.races[i]['id'], self.cars[1]['id']),
                    headers={'content-type': 'application/json'},
                    catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def assign_positions_to_drivers(self):
        """Assigns positions to drivers, such that positions are randomly generated"""

        positions = range(1, 21)
        for i in range(len(self.races)):
            pos1, pos2 = sample(positions, k=2)

            with self.client.patch(
                    url="http://localhost:8081/race/assignPointsDriverOne?raceId={}&position={}"
                            .format(self.races[i]['id'], pos1),
                    headers={'content-type': 'application/json'},
                    catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

            with self.client.patch(
                    url="http://localhost:8081/race/assignPointsDriverTwo?raceId={}&position={}"
                            .format(self.races[i]['id'], pos2),
                    headers={'content-type': 'application/json'},
                    catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser

    @task
    def get_best_driver_for_each_location(self):
        """Computes the best driver according to the historically gained points for each location"""

        for i in range(len(self.locations)):
            with self.client.get(
                    url="http://localhost:8081/race/findMostSuitableDriversForLocation?location={}"
                            .format(self.locations[i]),
                    headers={'content-type': 'application/json'},
                    catch_response=True) as response:

                if response.status_code == 200:
                    response.success()
                else:
                    response.failure(response.json())
                    raise StopUser


class ApplicationUser(HttpUser):
    tasks = [UserUseCase]
    wait_time = constant(1)
