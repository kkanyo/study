namespace CS_Study.Chapter06
{
    internal class Stereo
    {
        private string roomName;

        public Stereo(string roomName )
        {
            this.roomName = roomName;
        }

        internal void On()
        {
            Console.WriteLine( "Stereo on at " + roomName );
        }
        internal void Off()
        {
            Console.WriteLine( "Stereo off at " + roomName );
        }
        internal void SetCD()
        {
            Console.WriteLine( "CD mode at " + roomName );
        }
        internal void SetDVD()
        {
            Console.WriteLine( "DVD mode at " + roomName );
        }
        internal void SetRadio()
        {
            Console.WriteLine( "Radio mode at " + roomName );
        }
        internal void SetVolume(int vol)
        {
            Console.WriteLine( "Volume at " + roomName + " : " + vol );  
        }
    }
}
