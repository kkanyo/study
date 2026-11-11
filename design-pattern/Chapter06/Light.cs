namespace CS_Study.Chapter06
{
    internal class Light
    {
        private string roomName;

        public Light(string roomName)
        {
            this.roomName = roomName;
        }
        internal void On()
        {
            Console.WriteLine( "Light on at " + roomName );
        }

        internal void Off()
        {
            Console.WriteLine( "Light off at " + roomName );

        }
    }
}
