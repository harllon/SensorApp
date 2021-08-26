package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.SignupActivity;
import com.example.myapplication.databinding.FragmentHomeBinding;

import java.util.List;
import java.util.Objects;

import Utils.passwordHash;
import ViewModel.PersonViewModel;
import roomTest.Person;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding homeBinding;
    private PersonViewModel mPersonViewModel;
    private List<Person> listUsers;
    //private NavHostFragment navigation;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mPersonViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        mPersonViewModel.getAllPerson().observe(getViewLifecycleOwner(), new Observer<List<Person>>() { //change: this -> getView
            @Override
            public void onChanged(List<Person> people) {
                try{
                    listUsers = Objects.requireNonNull(people);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        SignIn();
        SignUp();
    }

    void SignUp(){
        homeBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean admin = false;
                Intent registerIntent = new Intent(getContext(), SignupActivity.class);
                registerIntent.putExtra("admin", admin);
                startActivity(registerIntent);
            }
        });
    }
    void SignIn(){
        homeBinding.signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checkUsername = false;
                Boolean checkPassword = false;
                Boolean checkAdmin = false;
                if(TextUtils.isEmpty(homeBinding.PasswordEditText.getText().toString()) || TextUtils.isEmpty(homeBinding.UserNameEditText.getText().toString())){
                    Toast.makeText(getContext(), "You must fill in all fields", Toast.LENGTH_LONG).show();
                }else{
                    for (int i = 0; i< listUsers.size(); i++){
                        if(listUsers.get(i).getUserName().equals(homeBinding.UserNameEditText.getText().toString())){
                            checkUsername = true;
                        }
                        if(checkUsername) {
                            String password = passwordHash.getMd5(homeBinding.PasswordEditText.getText().toString());
                            if (listUsers.get(i).getUserPassword().equals(password)){
                                checkPassword = true;
                                checkAdmin = listUsers.get(i).getIsAdmin();
                                break;
                            }
                        }
                    }
                }
                if(checkUsername && checkPassword){
                    //Toast.makeText(MainActivity.this, "The user exist", Toast.LENGTH_LONG).show();
                    if(checkAdmin){
                        //Intent adminIntent = new Intent(getContext(), AdminActivity.class);
                        //startActivity(adminIntent);
                        //navigation.getNavController().navigate(R.id.adminFragment);
                        Navigation.findNavController(requireView()).navigate(R.id.adminFragment);
                    }else{
                        //Intent normalIntent = new Intent(getContext(), NormalActivity.class);
                        //startActivity(normalIntent);
                        Navigation.findNavController(requireView()).navigate(R.id.normalFragment);
                    }
                }else{
                    Toast.makeText(getContext(), "Username or password is wrong.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}