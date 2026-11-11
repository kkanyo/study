namespace CS_Study.Chapter06
{
    internal class CeilingFanHighCommand : Command
    {
        private CeilingFan ceilingFan;
        private int prevSpeed;

        public CeilingFanHighCommand( CeilingFan ceilingFan )
        {
            this.ceilingFan = ceilingFan;
        }

        public void Execute()
        {
            prevSpeed = ceilingFan.GetSpeed();
            ceilingFan.High();
        }

        public void Undo()
        {
            switch (prevSpeed)
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
