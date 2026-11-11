namespace CS_Study.Chapter06
{
    internal class RemoteControl
    {
        const int NUM_COMMANDS = 7;
        Command[] onCommands;
        Command[] offCommands;
        Command undoCommand;

        public RemoteControl()
        {
            onCommands = new Command[ NUM_COMMANDS ];
            offCommands = new Command[ NUM_COMMANDS ];
            Command noCommand = new NoCommand();

            for (int i = 0; i < NUM_COMMANDS; i++)
            {
                onCommands[ i ] = noCommand;
                offCommands[ i ] = noCommand;
            }
            undoCommand = noCommand;
        }

        public void SetCommand(int slot, Command onCommand, Command offCommand)
        {
            onCommands[ slot ] = onCommand;
            offCommands[ slot ] = offCommand;
        }

        public void OnButtonWasPushed(int slot)
        {
            onCommands[ slot ].Execute();
            undoCommand = onCommands[ slot ];
        }

        public void OffButtonWasPushed(int slot)
        {
            offCommands[ slot ].Execute();
            undoCommand = offCommands[ slot ];
        }

        public void UndoButtonWasPushed()
        {
            undoCommand.Undo();
        }

        public override String ToString()
        {
            System.Text.StringBuilder stringBuild = new System.Text.StringBuilder();
            stringBuild.Append( "\n----- Remote control -----\n" );
            
            for (int i = 0; i < NUM_COMMANDS ; i++)
            {
                stringBuild.Append( "[slot " + i + "] " + onCommands[ i ].GetType().Name +
                    "   " + offCommands[ i ].GetType().Name + "\n" );
            }

            return stringBuild.ToString();
        }
    }
}
