import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Controller class.
 *
 * @author Charlie Vukovic
 */
public final class NNCalcController1 implements NNCalcController {

    /**
     * Model object.
     */
    private final NNCalcModel model;

    /**
     * View object.
     */
    private final NNCalcView view;

    /**
     * Useful constants.
     */
    private static final NaturalNumber TWO = new NaturalNumber2(2),
            INT_LIMIT = new NaturalNumber2(Integer.MAX_VALUE);

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @ensures <pre>
     * {@code [this.view has been updated to be consistent with this.model]}
     * </pre>
     */
    private void updateViewToMatchModel() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        //update subtract button
        if (bottom.compareTo(top) > 0) {
            this.view.updateSubtractAllowed(false);
        } else {
            this.view.updateSubtractAllowed(true);
        }

        // for updating power
        if (bottom.compareTo(INT_LIMIT) <= 0) {
            this.view.updatePowerAllowed(true);
        } else {
            this.view.updatePowerAllowed(false);
        }

        // for updating divide
        if (bottom.isZero()) {
            this.view.updateDivideAllowed(false);
        } else {
            this.view.updateDivideAllowed(true);
        }

        // for updating root
        if (bottom.compareTo(TWO) >= 0 && bottom.compareTo(INT_LIMIT) <= 0) {
            this.view.updateRootAllowed(true);
        } else {
            this.view.updateRootAllowed(false);
        }
        // update view
        this.view.updateTopDisplay(top);
        this.view.updateBottomDisplay(bottom);
    }

    /**
     * Constructor.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public NNCalcController1(NNCalcModel model, NNCalcView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void processClearEvent() {

        NaturalNumber bottom = this.model.bottom();
        bottom.clear();
        // update view
        this.updateViewToMatchModel();
    }

    @Override
    public void processSwapEvent() {
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        top.transferFrom(bottom);
        bottom.transferFrom(temp);
        // update view
        this.updateViewToMatchModel();
    }

    @Override
    public void processEnterEvent() {
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.copyFrom(bottom);

        // update view
        this.updateViewToMatchModel();
    }

    @Override
    public void processAddEvent() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        bottom.add(top);
        top.clear();
        // update view
        this.updateViewToMatchModel();
    }

    @Override
    public void processSubtractEvent() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.subtract(bottom);
        bottom.transferFrom(top);
        // update view
        this.updateViewToMatchModel();
    }

    @Override
    public void processMultiplyEvent() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.multiply(bottom);
        bottom.transferFrom(top);
        // update view
        this.updateViewToMatchModel();
    }

    @Override
    public void processDivideEvent() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        NaturalNumber r = top.divide(bottom);
        bottom.transferFrom(top);
        top.transferFrom(r);
        // update view
        this.updateViewToMatchModel();
    }

    @Override
    public void processPowerEvent() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.power(bottom.toInt());
        bottom.transferFrom(top);
        // update view
        this.updateViewToMatchModel();
    }

    @Override
    public void processRootEvent() {

        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();

        top.root(bottom.toInt());
        bottom.transferFrom(top);
        // update view
        this.updateViewToMatchModel();
    }

    @Override
    public void processAddNewDigitEvent(int digit) {

        NaturalNumber bottom = this.model.bottom();

        bottom.multiplyBy10(digit);
        // update view
        this.updateViewToMatchModel();
    }

}
