package c8y;

import java.util.ArrayList;
import java.util.List;

import org.svenson.JSONTypeHint;

/**
 * New software list fragment implementation. This is a {@link List} of {@link SoftwareItem SoftwareItems}.
 *
 * For usage as a fragment in another class the setter/getter must be annotated with the {@link JSONTypeHint @JSONTypeHint} annotation according to the svenson specifications.
 *
 * See {@link SoftwareListContainer} for an example.
 */
public class SoftwareList extends ArrayList<SoftwareItem> {

}
