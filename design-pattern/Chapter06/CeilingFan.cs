namespace CS_Study.Chapter06
{
    public enum FanSet
    {
        OFF = 0,
        LOW,
        MEDIUM,
        HIGH
    };

    internal class CeilingFan
    {
        private string location;
        private int speed;

        internal CeilingFan( String location )
        {
            this.location = location;
            speed = ( int )FanSet.OFF;
        }

        internal void High()
        {
            speed = ( int )FanSet.HIGH;
            Console.WriteLine( location + " ceiling fan is on high" );
        }

        internal void Medium()
        {
            speed = ( int )FanSet.MEDIUM;
            Console.WriteLine( location + " ceiling fan is on medium" );
        }

        internal void Low()
        {
            speed = ( int )FanSet.LOW;
            Console.WriteLine( location + " ceiling fan is on low" );
        }

        internal void Off()
        {
            speed = ( int )FanSet.OFF;
            Console.WriteLine( location + " ceiling fan is off" );
        }

        internal int GetSpeed()
        {
            return speed;
        }
    }

}
