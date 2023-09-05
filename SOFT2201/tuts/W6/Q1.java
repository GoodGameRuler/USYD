import java.util.*;

public class Race {
		private List<Racecar> cars = new ArrayList<>();
		public List<Double> displayData(DisplayMethod method) {
				List<Double> times = new ArrayList<>();
				if(method == DisplayMethod.FastestLapTime) {
						for(Racecar car : cars) {
								times.add(car.fastestLapTime());
						}
						Collections.sort(times);
				} else if(method == DisplayMethod.FastestRaceTime) {
						for(Racecar car : cars) {
								times.add(car.raceTime());
						}
						Collections.sort(times);
				} else if(method == DisplayMethod.SlowestRaceTime) {
						for(Racecar car : cars) {
								times.add(car.raceTime());
						}
						Collections.sort(times);
						Collections.reverse(times);
				}
				return times;
		}
}

class RaceCar {
	private ArrayList<float> lapTimes = new ArrayList<float>();

	public RaceCar() {
	}

	public void finishLap() {
		lapTimes.add(system.currentTimeMills()/1000.0)
	}

	
	public float raceTime() {
		return lapTimes.get(lapTimes.size() - 1) - lapTimes.get(0);
	}

	public float fastestLapTime() {
		float currentFastest
	
}

enum DisplayMethod {
	FastestLapTime,
	RaceTime,
	SlowestRaceTime,

}
