package co.nickp.monde.android.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.nickp.monde.android.theme.databinding.FragmentTypographyBinding

class TypographyFragment : Fragment() {
    private var _binding: FragmentTypographyBinding? = null
    private val binding get() = _binding!! // valid from onCreateView to onDestroyView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTypographyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
