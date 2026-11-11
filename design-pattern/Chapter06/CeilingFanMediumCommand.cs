namespace CS_Study.Chapter06
{
    internal class CeilingFanMediumCommand : Command
    {
        private CeilingFan ceilingFan;
        private int prevSpeed;

        public CeilingFanMediumCommand( CeilingFan ceilingFan )
        {
            this.ceilingFan = ceilingFan;
        }

        public void Execute()
        {
            prevSpeed = ceilingFan.GetSpeed();
            ceilingFan.Medium();
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
