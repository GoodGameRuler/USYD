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
