package invaders.observer;

public class Timer extends CounterSubject {

    private int frames = 0;

    private int seconds = 0;
    private int minutes = 0;

    public int getFrames() {
        return this.frames;

    }

    public String getTime() {
        return minutes + ":" + seconds;
    }

    public void setFrames(int frames) {
        this.seconds = frames / 60;
        this.minutes = this.seconds / 60;
        this.seconds = this.seconds % 60;
        this.frames = frames;
    }

    public void incrementTime() {
        this.frames++;

        if(this.frames % 60 == 0) {
            this.seconds++;

            if(this.seconds == 60) {
                this.seconds = 0;
                this.minutes++;
            }
        }
    }
}
