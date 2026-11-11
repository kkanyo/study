namespace CS_Study.Chapter02
{
    public interface IObserver
    {
        public void Update( float temp, float humidity, float pressure );
        public void Update();
    }
}
