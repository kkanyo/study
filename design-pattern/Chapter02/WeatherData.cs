using System.Collections;

namespace CS_Study.Chapter02
{
    public class WeatherData : ISubject
    {

        private List<IObserver> observers;
        private float temperature;
        private float humidity;
        private float pressure;

        public WeatherData()
        {
            observers = new List<IObserver>();
        }

        public void RegisterObserver( IObserver o )
        {
            observers.Add( o );
        }

        public void RemoveObserver( IObserver o )
        {
            observers.Remove( o );
        }

        public void NotifyObservers()
        {
            foreach ( IObserver observer in observers )
            {
                // 주제가 옵저거에게 상태를 알리는 방식(Push)
                //observer.Update( temperature, humidity, pressure );

                // 옵저버가 주제로부터 상태를 끌어오는 방식(Pull)
                observer.Update();
            }
        }

        public void MeasurementsChanged()
        {
            NotifyObservers();
        }

        public void SetMeasurements( float temperature, float humidity, float pressure )
        {
            this.temperature = temperature;
            this.humidity = humidity;
            this.pressure = pressure;
            MeasurementsChanged();
        }

        public float GetTemperature()
        {
            return temperature;
        }

        public float GetHumidity()
        {
            return humidity;
        }

        public float GetPressure()
        {
            return pressure;
        }
    }
}

