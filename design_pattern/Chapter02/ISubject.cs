namespace CS_Study.Chapter02
{
    public interface ISubject
    {
        public void RegisterObserver( IObserver o );
        public void RemoveObserver( IObserver o );
        public void NotifyObservers();
    }
}