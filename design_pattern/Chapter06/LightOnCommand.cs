namespace CS_Study.Chapter06
{
    internal class LightOnCommand : Command
    {
        Light light;

        // 커맨드 객체로 제어할 리시버의 정보 전달
        public LightOnCommand(Light light)
        {
            this.light = light;
        }

        public void Execute()
        {
            light.On();
        }

        public void Undo()
        {
            light.Off();
        }
    }
}
