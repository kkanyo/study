namespace CS_Study.Chapter07_2
{
    internal class HomeTheaterFacade
    {
        Amplifier amp;
        Tuner tuner;
        StreamingPlayer player;
        Projector projector;
        TheaterLights lights;
        Screen screen;
        PopcornPopper popper;

        public HomeTheaterFacade( Amplifier amp,
            Tuner tuner,
            StreamingPlayer player,
            Projector projector,
            TheaterLights lights,
            Screen screen,
            PopcornPopper popper )
        {
            this.amp = amp;
            this.tuner = tuner;
            this.player = player;
            this.projector = projector; 
            this.lights = lights;
            this.screen = screen;
            this.popper = popper;
        }

        public void WatchMovie(String movie)
        {
            Console.WriteLine( "Ready to watch movie" );
            //popper.On();
            //popper.Pop();
            //light.dim( 10 );
            //...
        }

        public void EndMovie()
        {
            Console.WriteLine( "Turnning off a home-theater" );
            //popper.Off();
            //lights.On();
            //screen.Up();
            //...
        }
    }
}
