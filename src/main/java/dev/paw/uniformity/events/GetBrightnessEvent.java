package dev.paw.uniformity.events;

public class GetBrightnessEvent extends Event {
    private float brightness;
    public GetBrightnessEvent(float brightness) {
        this.brightness = brightness;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}
