package averageJoes.view

import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import java.awt.{BorderLayout, Dimension, FlowLayout, GridLayout}

import averageJoes.common.{ControllerSearch, ServerSearch}
import averageJoes.model.hardware.HardwareController.Msg
import averageJoes.model.workout.MachineTypes.MachineType
import averageJoes.model.hardware.PhysicalMachine.MachineLabel
import averageJoes.model.hardware.{Device, HardwareController, PhysicalMachine}
import averageJoes.model.workout.{MachineTypeConverters, MachineTypes}
import akka.actor.typed.{ActorRef, ActorSystem}
import javax.swing._



object View extends App {
    private val machinePanel: MachineView = MachineView()
    private val userPanel: UserView = UserView()

    val frame = new JFrame("AverageJoe's")
    frame.setSize(new Dimension(1200, 600))

    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)


    val mainPanel = new JPanel(new BorderLayout())
    mainPanel.add(machinePanel,BorderLayout.CENTER)
    mainPanel.add(userPanel,BorderLayout.WEST)

    viewInit()

    frame.add(mainPanel)
    frame.addWindowListener(new WindowAdapter(){
       override def windowClosing(ev:WindowEvent){
           ServerSearch.closeSystem()
           ControllerSearch.closeSystem()
            System.exit(0);
        }
        override def windowClosed(ev:WindowEvent){
            System.exit(0);
        }
    });

    def _getMachineView(): MachineView = machinePanel
    def _getUserView(): JPanel = userPanel

    def viewInit(): Unit = {
        val controller = ControllerSearch.getController
        controller ! Msg.CreatePhysicalMachine("LegPress1", MachineTypes.LEG_PRESS, "LegPress A")
        controller ! Msg.CreatePhysicalMachine("Cycling", MachineTypes.CYCLING, "Cycling A")
        controller ! Msg.CreatePhysicalMachine("ChestFly1", MachineTypes.CHEST_FLY, "ChestFly")
        controller ! Msg.CreatePhysicalMachine("Lifting", MachineTypes.LIFTING, "Lifting")
        controller ! Msg.CreatePhysicalMachine("Running1", MachineTypes.RUNNING, "Running A")
        controller ! Msg.CreatePhysicalMachine("Running2", MachineTypes.RUNNING, "Running B")

        controller ! Msg.CreateDevice("Wristband1", Device.DeviceType.wristband, "Wristband1")
        controller ! Msg.CreateDevice("Wristband2", Device.DeviceType.wristband, "Wristband2")
        controller ! Msg.CreateDevice("Wristband3", Device.DeviceType.wristband, "Wristband3")
        controller ! Msg.CreateDevice("Wristband4", Device.DeviceType.wristband, "Wristband4")
    }
    Thread.sleep(1000)
    frame.setVisible(true)

}

case class UserView() extends JPanel(new GridLayout(10,1))

case class MachineView() extends JPanel(new GridLayout(3,3)){
    private var map:Map[String, ActorRef[PhysicalMachine.Msg]] = Map.empty

    def addEntry(name:String, actorRef: ActorRef[PhysicalMachine.Msg]):Unit =
        map += (name -> actorRef)

    def _getMapKeyList(): List[String] = map.keySet.toList

    def _getMapValue(key:String): Option[ActorRef[PhysicalMachine.Msg]] = map.get(key)
}


case class UserGui(deviceActor: ActorRef[Device.Msg], deviceLabel: String) extends JPanel(new GridLayout(2,1)) {
    val dimension =  new Dimension(300, 600)
    this.setPreferredSize(dimension)
    private val button = new JButton(deviceLabel)
    private val text = new JTextArea()
    private var physicalActor: Option[ActorRef[PhysicalMachine.Msg]] = None

    add(button)
    add(text)

    button.addActionListener((e: ActionEvent) => {
        val array = View._getMachineView()._getMapKeyList().toArray
        val machineChoice =  JOptionPane.showInputDialog(
            this,
            "Choose Machine",
            "Machines To Connect",
            JOptionPane.PLAIN_MESSAGE,
            null,
            View._getMachineView()._getMapKeyList().toArray,
            0)


        machineChoice match {
            case a: String => physicalActor = View._getMachineView()._getMapValue(a)
                /*Send physical machine path to device*/
                deviceActor ! Device.Msg.NearDevice(physicalActor.get)
            case _ =>
        }
    })


    def update (msg: String): Unit = {
        text.setText(msg)
    }
}

case class MachineGUI(machineLabel: MachineLabel, machineType: MachineType, actorRef: ActorRef[PhysicalMachine.Msg])
    extends JPanel(new GridLayout(3,1)){
    private val start = new JButton("START")
    private val label = new JLabel(machineLabel)
    private val text = new JTextArea()
    private var map:Map[String, JTextField] = Map.empty

    private val panel: JPanel = new JPanel(new GridLayout(1,2)){
        this.setPreferredSize( new Dimension(200, 20))
        add(label)
        add(start)
    }
    add(panel)

    val panelParameters = new JPanel(new GridLayout(1,4))
    MachineTypeConverters.setParametersView(machineType).foreach(x => {
        val dimension = new Dimension(100, 20)
        val label = new JLabel(x) {
            setPreferredSize(dimension)
        }
        val textParameter = new JTextField(){
            setPreferredSize(dimension)
        }

        val flowPanel = new JPanel(new FlowLayout()){
             add(label)
             add(textParameter)
         }
        panelParameters.add(flowPanel)
        map += (label.getText -> textParameter)

    })

    add(panelParameters)

    add(text)
    start.addActionListener((e: ActionEvent) => {
        try {
            if(getParameters != Nil){
                actorRef ! PhysicalMachine.Msg.StartExercise(getParameters)
                setButton(true)
            }
        } catch {
            case _: NumberFormatException =>
                JOptionPane.showMessageDialog(this,
                    "Insert correct value",
                    "INPUT ERROR",
                    JOptionPane.ERROR_MESSAGE)
        }
    })


     def update (msg: String): Unit = {
        text.setText(msg)
    }

    def setParameters(listParameters: List[(String,Int)]): Unit = {
        listParameters.foreach(x => {
            map(x._1).setText(x._2.toString)
        })
    }

     def getParameters: List[(String, Int)]  = {
        var tmp: Map[String, Int] = Map.empty
        map foreach(x => {
            if(x._2.getText() != "" && x._2.getText().length < 4 && x._2.getText().toInt != 0){
                tmp += (x._1 -> x._2.getText().toInt)
            } else {
                throw new NumberFormatException
            }
        })
        tmp.toList
    }

    def setButton(boolean: Boolean): Unit = {
        start.setEnabled(boolean)
    }

    def clearFields(): Unit = {
        map foreach(x => x._2.setText(""))
    }



}
