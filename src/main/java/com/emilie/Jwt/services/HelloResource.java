package com.emilie.Jwt.services;

import com.emilie.Jwt.models.AuthenticationRequest;
import com.emilie.Jwt.models.AuthenticationResponse;
import com.emilie.Jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @RequestMapping(value="/authenticate", method=RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            System.out.println(authenticationRequest.getUsername());
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken( authenticationRequest.getUsername(), authenticationRequest.getPassword() )
            );
        } catch (BadCredentialsException e) {
            throw new Exception( "Incorrect username or password", e );
        }
        final UserDetails userDetails=userDetailsService
                .loadUserByUsername( authenticationRequest.getUsername() );

        final String jwt=jwtTokenUtil.generateToken( userDetails );

        return ResponseEntity.ok( new AuthenticationResponse( jwt ) );

    }


}
