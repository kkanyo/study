namespace CS_Study.Chapter02
{
    public class StatisticsDisplay : IObserver, IDisplayElement
    {
        private float maxTemp = 0.0f;
        private float minTemp = 200;
        private float tempSum = 0.0f;
        private int numReadings;
        private WeatherData weatherData;

        public StatisticsDisplay( WeatherData weatherData )
        {
            this.weatherData = weatherData;
            weatherData.RegisterObserver( this );
        }

        public void Update( float temp, float humidity, float pressure )
        {
            tempSum += temp;
            numReadings++;

            if ( temp > maxTemp )
            {
                maxTemp = temp;
            }

            if ( temp < minTemp )
            {
                minTemp = temp;
            }

            Display();
        }

        public void Update()
        {
            float temp = weatherData.GetTemperature();

            this.tempSum += temp;
            this.numReadings++;

            if ( temp > this.maxTemp )
            {
                this.maxTemp = temp;
            }

            if ( temp < this.minTemp )
            {
                this.minTemp = temp;
            }

            Display();
        }

        public void Display()
        {
            Console.WriteLine( "Avg/Max/Min temperature = " + ( tempSum / numReadings )
                + "/" + maxTemp + "/" + minTemp );
        }
    }
}