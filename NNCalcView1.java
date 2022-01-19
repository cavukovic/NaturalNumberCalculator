import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import components.naturalnumber.NaturalNumber;

/**
 * View class.
 *
 * @author Charlie Vukovic
 */
public final class NNCalcView1 extends JFrame implements NNCalcView {

    /**
     * Controller object registered with this view to observe user-interaction
     * events.
     */
    private NNCalcController controller;

    /**
     * State of user interaction: last event "seen".
     */
    private static enum State {
        /**
         * Last event was clear, enter, another operator, or digit entry, resp.
         */
        SAW_CLEAR, SAW_ENTER, SAW_OTHER_OP, SAW_DIGIT
    }

    /**
     * State variable to keep track of which event happened last; needed to
     * prepare for digit to be added to bottom operand.
     */
    private State currentState;

    /**
     * Text areas.
     */
    private final JTextArea topText, bottomText;

    /**
     * Operator and related buttons.
     */
    private final JButton clear, swap, enter, add, subtract, multiply, divide,
            power, root;

    /**
     * Digit entry buttons.
     */
    private final JButton[] digits = new JButton[10];

    /**
     * Useful constants.
     */
    private static final int TEXT_AREA_HEIGHT = 5, TEXT_AREA_WIDTH = 20,
            DIGIT_BUTTONS = 10, MAIN_BUTTON_PANEL_GRID_ROWS = 4,
            MAIN_BUTTON_PANEL_GRID_COLUMNS = 4, SIDE_BUTTON_PANEL_GRID_ROWS = 3,
            SIDE_BUTTON_PANEL_GRID_COLUMNS = 1, CALC_GRID_ROWS = 3,
            CALC_GRID_COLUMNS = 1;

    /**
     * Default constructor.
     */
    public NNCalcView1() {
        // Create the JFrame being extended

        /*
         * Call the JFrame (superclass) constructor with a String parameter to
         * name the window in its title bar
         */
        super("Natural Number Calculator");

        // Set up the GUI widgets --------------------------------------------

        /*
         * Set up initial state of GUI to behave like last event was "Clear";
         * currentState is not a GUI widget per se, but is needed to process
         * digit button events appropriately
         */
        this.currentState = State.SAW_CLEAR;
        /*
         * Create widgets
         */
        this.topText = new JTextArea("", TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        this.bottomText = new JTextArea("", TEXT_AREA_HEIGHT, TEXT_AREA_WIDTH);
        this.clear = new JButton("Clear");
        this.swap = new JButton("Swap");
        this.enter = new JButton("Enter");
        this.add = new JButton("Add");
        this.subtract = new JButton("Subtract");
        this.multiply = new JButton("Multiply");
        this.divide = new JButton("Divide");
        this.power = new JButton("Power");
        this.root = new JButton("Root");
        for (int count = 0; count < DIGIT_BUTTONS; count++) {
            this.digits[count] = new JButton(String.valueOf(count));
        }
        // Set up the GUI widgets --------------------------------------------

        /*
         * Text areas should wrap lines, and should be read-only; they cannot be
         * edited because allowing keyboard entry would require checking whether
         * entries are digits, which we don't want to have to do
         */
        this.topText.setEditable(false);
        this.topText.setLineWrap(true);
        this.topText.setWrapStyleWord(true);
        this.bottomText.setEditable(false);
        this.bottomText.setLineWrap(true);
        this.bottomText.setWrapStyleWord(true);
        /*
         * Initially, the following buttons should be disabled: divide (divisor
         * must not be 0) and root (root must be at least 2) -- hint: see the
         * JButton method setEnabled
         */
        this.divide.setEnabled(false);
        this.root.setEnabled(false);
        /*
         * Create scroll panes for the text areas in case number is long enough
         * to require scrolling
         */
        JScrollPane tTopScrollPane = new JScrollPane(this.topText);
        JScrollPane tBottomScrollPane = new JScrollPane(this.bottomText);
        /*
         * Create main button panel
         */
        JPanel buttonPanel = new JPanel(new GridLayout(
                MAIN_BUTTON_PANEL_GRID_ROWS, MAIN_BUTTON_PANEL_GRID_COLUMNS));
        /*
         * Add the buttons to the main button panel, from left to right and top
         * to bottom
         */
        buttonPanel.add(this.digits[7]);
        buttonPanel.add(this.digits[8]);
        buttonPanel.add(this.digits[9]);
        buttonPanel.add(this.add);
        buttonPanel.add(this.digits[4]);
        buttonPanel.add(this.digits[5]);
        buttonPanel.add(this.digits[6]);
        buttonPanel.add(this.subtract);
        buttonPanel.add(this.digits[1]);
        buttonPanel.add(this.digits[2]);
        buttonPanel.add(this.digits[3]);
        buttonPanel.add(this.multiply);
        buttonPanel.add(this.digits[0]);
        buttonPanel.add(this.power);
        buttonPanel.add(this.root);
        buttonPanel.add(this.divide);
        /*
         * Create side button panel
         */
        JPanel sidebuttonPanel = new JPanel(new GridLayout(
                SIDE_BUTTON_PANEL_GRID_ROWS, SIDE_BUTTON_PANEL_GRID_COLUMNS));
        /*
         * Add the buttons to the side button panel, from left to right and top
         * to bottom
         */
        sidebuttonPanel.add(this.clear);
        sidebuttonPanel.add(this.swap);
        sidebuttonPanel.add(this.enter);
        /*
         * Create combined button panel organized using flow layout, which is
         * simple and does the right thing: sizes of nested panels are natural,
         * not necessarily equal as with grid layout
         */
        JPanel combinedbuttonPanel = new JPanel(new FlowLayout());
        /*
         * Add the other two button panels to the combined button panel
         */
        combinedbuttonPanel.add(buttonPanel);
        combinedbuttonPanel.add(sidebuttonPanel);
        /*
         * Organize main window
         */
        this.setLayout(new GridLayout(CALC_GRID_ROWS, CALC_GRID_COLUMNS));
        /*
         * Add scroll panes and button panel to main window, from left to right
         * and top to bottom
         */
        this.add(tTopScrollPane);
        this.add(tBottomScrollPane);
        this.add(combinedbuttonPanel);
        // Set up the observers ----------------------------------------------

        /*
         * Register this object as the observer for all GUI events
         */
        this.clear.addActionListener(this);
        this.swap.addActionListener(this);
        this.enter.addActionListener(this);
        this.add.addActionListener(this);
        this.subtract.addActionListener(this);
        this.multiply.addActionListener(this);
        this.divide.addActionListener(this);
        this.power.addActionListener(this);
        this.root.addActionListener(this);
        for (int count = 0; count < DIGIT_BUTTONS; count++) {
            this.digits[count].addActionListener(this);
        }
        // Set up the main application window --------------------------------

        /*
         * Make sure the main window is appropriately sized, exits this program
         * on close, and becomes visible to the user
         */
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void registerObserver(NNCalcController controller) {
        this.controller = controller;
    }

    @Override
    public void updateTopDisplay(NaturalNumber n) {
        String num = n.toString();
        this.topText.setText(num);
    }

    @Override
    public void updateBottomDisplay(NaturalNumber n) {
        String num = n.toString();
        this.bottomText.setText(num);
    }

    @Override
    public void updateSubtractAllowed(boolean allowed) {
        this.subtract.setEnabled(allowed);
    }

    @Override
    public void updateDivideAllowed(boolean allowed) {
        this.divide.setEnabled(allowed);
    }

    @Override
    public void updatePowerAllowed(boolean allowed) {
        this.power.setEnabled(allowed);
    }

    @Override
    public void updateRootAllowed(boolean allowed) {
        this.root.setEnabled(allowed);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        /*
         * Set cursor to indicate computation on-going; this matters only if
         * processing the event might take a noticeable amount of time as seen
         * by the user
         */
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        /*
         * Determine which event has occurred that we are being notified of by
         * this callback; in this case, the source of the event (i.e, the widget
         * calling actionPerformed) is all we need because only buttons are
         * involved here, so the event must be a button press; in each case,
         * tell the controller to do whatever is needed to update the model and
         * to refresh the view
         */
        Object source = event.getSource();
        if (source == this.clear) {
            this.controller.processClearEvent();
            this.currentState = State.SAW_CLEAR;
        } else if (source == this.swap) {
            this.controller.processSwapEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.enter) {
            this.controller.processEnterEvent();
            this.currentState = State.SAW_ENTER;
        } else if (source == this.add) {
            this.controller.processAddEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.subtract) {
            this.controller.processSubtractEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.multiply) {
            this.controller.processMultiplyEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.divide) {
            this.controller.processDivideEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.power) {
            this.controller.processPowerEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else if (source == this.root) {
            this.controller.processRootEvent();
            this.currentState = State.SAW_OTHER_OP;
        } else {
            for (int i = 0; i < DIGIT_BUTTONS; i++) {
                if (source == this.digits[i]) {
                    switch (this.currentState) {
                        case SAW_ENTER:
                            this.controller.processClearEvent();
                            break;
                        case SAW_OTHER_OP:
                            this.controller.processEnterEvent();
                            this.controller.processClearEvent();
                            break;
                        default:
                            break;
                    }
                    this.controller.processAddNewDigitEvent(i);
                    this.currentState = State.SAW_DIGIT;
                    break;
                }
            }
        }
        /*
         * Set the cursor back to normal (because we changed it at the beginning
         * of the method body)
         */
        this.setCursor(Cursor.getDefaultCursor());
    }

}
