namespace CS_Study.Chapter06
{
    internal class StereoOnWithCDCommand : Command
    {
        Stereo stereo;

        public StereoOnWithCDCommand(Stereo stereo )        {
            this.stereo = stereo;
        }

        public void Execute()
        {
            stereo.On();
            stereo.SetCD();
            stereo.SetVolume( 11 );
        }

        public void Undo()
        {

        }
    }
}
