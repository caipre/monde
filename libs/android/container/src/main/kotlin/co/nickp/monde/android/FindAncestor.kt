package co.nickp.monde.android

import androidx.fragment.app.Fragment

/**
 *  Walk up the fragment, activity, application hierarchy to find an
 *  instance that implements the given type T. This ensures that the
 *  fragment is decoupled from the hosting activity.
 *  @throws AssertionError if no ancestor implements T
 */
inline fun <reified T> Fragment.findAncestor(): T {
  var curr: Fragment? = this
  while (curr != null) {
    if (curr is T) return curr
    curr = curr.parentFragment
  }
  return (this.activity as? T)
      ?: (this.activity?.application as? T)
      ?: throw AssertionError("No ancestor implements ${T::class.qualifiedName}")
}
