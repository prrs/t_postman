package io.branch.referral;

/**
 * <p>
 * Class for representing any Branch SDK exceptions.
 * Exception details are provided in exception message.
 * </p>
 */
public class BranchException extends Exception {

    /*
	 * Generated Serial ID for BranchException.
	 */
	private static final long serialVersionUID = 158893884667629597L;

	private String branchErrorMsg = ""; /* Branch Specific message for this error. */

    /*
     *  Message for Instance not available error. Should be thrown when user trying to access
     *  {@link Branch} instance when the instance is not yet created. This is applicable only in
     *  case of calling getInstance() without instantiating {@link BranchApp}.
     */
    private static final String BRANCH_INSTANTIATION_ERR_MSG = "Branch instance is not created." +
            " Make  sure your Application class is an instance of BranchLikedApp.";


   /*
    *  Message for API level support not available error. Should be thrown on instantiating {@link BranchApp}
    *  on API level below 14.
    */
    public static final String BRANCH_API_LVL_ERR_MSG = "BranchApp class can be used only" +
            " with API level 14 or above. Please make sure your minimum API level supported is 14." +
            " If you wish to use API level below 14 consider calling getInstance(Context) instead.";


    BranchException(String message) {
        branchErrorMsg = message;
    }

    public static BranchException getInstantiationException() {
        return new BranchException(BRANCH_INSTANTIATION_ERR_MSG);
    }

    public static BranchException getAPILevelException() {
        return new BranchException(BRANCH_API_LVL_ERR_MSG);
    }

    @Override
    public String getMessage() {
        return branchErrorMsg;
    }

}
