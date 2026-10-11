import utils.File;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20C.*;
import static utils.FileTypes.SHADER;

public class Shader {

    public int id;

    private final File vsFile;
    private final File fsFile;

    public Map<String, Integer> uLocations;
    public Map<String, Object> uniforms;

    public Shader( String name ) {
        vsFile = new File( SHADER, name, "vert" );
        fsFile = new File( SHADER, name, "frag" );

        uLocations = new HashMap<>();
        uniforms = new HashMap<>();

        id = compile();
        glUseProgram( id );
    }

    public void set1f( String name, float value ) {
        glUseProgram( id );
        if ( !uLocations.containsKey( name ) ) {
            createUniform( name );
            uniforms.put( name, value );
        }
        uniforms.replace( name, value );
        setUniform1f( name, value );
        glUseProgram( 0 );
    }

    public void set1i( String name, int value ) {
        glUseProgram( id );
        if ( !uLocations.containsKey( name ) ) {
            createUniform( name );
            uniforms.put( name, value );
        }
        uniforms.replace( name, value );
        setUniform1i( name, value );
        glUseProgram( 0 );
    }

    private void createUniform( String name ) {
        int uLocation = glGetUniformLocation( id, name );
        if ( uLocation == -1 ) {
            uLocations.remove( name );
            uniforms.remove( name );
            return;
        }
        uLocations.put( name, uLocation );
    }

    private void setUniform1f( String name, float value ) {
        Integer uLocation = uLocations.get( name );
        if ( uLocation == null ) {
            return;
        }
        glUniform1f( uLocation, value );
    }

    private void setUniform1i( String name, int value ) {
        Integer uLocation = uLocations.get( name );
        if ( uLocation == null ) {
            return;
        }
        glUniform1i( uLocation, value );
    }

    public void recompile() {
        if ( fsFile.isModified() || vsFile.isModified() ) {
            System.out.println( "Recompiling shaders..." );

            int newId = compile();
            if ( newId == -1 ) return;

            glDeleteProgram( id );
            id = newId;
            glUseProgram( id );

            setUniforms();
        }
    }

    private void setUniforms() {
        for ( Map.Entry<String, Object> entry : uniforms.entrySet() ) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if ( value instanceof Integer ) {
                set1i( key, ( int ) value );
            }
            if ( value instanceof Float ) {
                set1f( key, ( float ) value );
            }
        }
    }

    private int compile() {
        int newId = glCreateProgram();

        String vss = vsFile.read();
        String fss = fsFile.read();

        int vertexShader = glCreateShader( GL_VERTEX_SHADER );
        glShaderSource( vertexShader, vss );
        glCompileShader( vertexShader );
        if ( glGetShaderi( vertexShader, GL_COMPILE_STATUS ) == 0 ) {
            System.err.println( glGetShaderInfoLog( vertexShader ) );
            glDeleteProgram( newId );
            glDeleteShader( vertexShader );
            return -1;
        }

        int fragmentShader = glCreateShader( GL_FRAGMENT_SHADER );
        glShaderSource( fragmentShader, fss );
        glCompileShader( fragmentShader );
        if ( glGetShaderi( fragmentShader, GL_COMPILE_STATUS ) == 0 ) {
            System.err.println( glGetShaderInfoLog( fragmentShader ) );
            glDeleteProgram( newId );
            glDeleteShader( fragmentShader );
            return -1;
        }

        glAttachShader( newId, vertexShader );
        glAttachShader( newId, fragmentShader );
        glLinkProgram( newId );
        glDeleteShader( vertexShader );
        glDeleteShader( fragmentShader );
        if ( glGetProgrami( newId, GL_LINK_STATUS ) == 0 ) {
            System.err.println( glGetProgramInfoLog( newId ) );
            glDeleteProgram( newId );
            return -1;
        }
        return newId;
    }
}
