namespace CS_Study.Chapter06
{
    internal class CeilingFanOffCommand : Command
    {
        private CeilingFan ceilingFan;
        private int prevSpeed;

        public CeilingFanOffCommand( CeilingFan ceilingFan )
        {
            this.ceilingFan = ceilingFan;
        }

        public void Execute()
        {
            prevSpeed = ceilingFan.GetSpeed();
            ceilingFan.Off();
        }

        public void Undo()
        {
            switch ( prevSpeed )
            {
                case ( int )FanSet.HIGH:
                    ceilingFan.High();
                    break;
                case ( int )FanSet.MEDIUM:
                    ceilingFan.Medium();
                    break;
                case ( int )FanSet.LOW:
                    ceilingFan.Low();
                    break;
                case ( int )FanSet.OFF:
                    ceilingFan.Off();
                    break;
            }
        }
    }
}
