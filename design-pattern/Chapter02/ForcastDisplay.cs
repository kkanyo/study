namespace CS_Study.Chapter02
{
    public class ForcastDisplay : IObserver, IDisplayElement
    {
        private float currentPressure = 29.92f;
        private float lastPressure;
        private WeatherData weatherData;

        public ForcastDisplay(WeatherData weatherData)
        {
            this.weatherData = weatherData;
            weatherData.RegisterObserver(this);
        }

        public void Update( float temp, float humidity, float pressure )
        {
            lastPressure = this.currentPressure;
            this.currentPressure = pressure;

            Display();
        }

        public void Update()
        {
            lastPressure = this.currentPressure;
            this.currentPressure = weatherData.GetPressure();

            Display();
        }

        public void Display()
        {
            if (lastPressure < currentPressure)
            {
                Console.WriteLine( "Improving weather on the way!" );
            }
            else if (lastPressure > currentPressure)
            {
                Console.WriteLine( "Watch out for cooler, rainy weather" );
            }
            else
            {
                Console.WriteLine( "More of the same" );
            }
        }
    }
}