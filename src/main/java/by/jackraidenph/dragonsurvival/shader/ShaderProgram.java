package by.jackraidenph.dragonsurvival.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ShaderProgram {
    private int programID; 

    public ShaderProgram()
    {
        programID = ARBShaderObjects.glCreateProgramObjectARB();
    }

    public ShaderProgram addFragment(String path)
    {
        return add(path, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
    }

    public ShaderProgram addVertex(String path)
    {
        return add(path, ARBVertexShader.GL_VERTEX_SHADER_ARB);
    }


    public ShaderProgram add(String path, int shaderType)
    {
        int shaderID = ARBShaderObjects.glCreateShaderObjectARB(shaderType); 
        ARBShaderObjects.glShaderSourceARB(shaderID, readFile(path));
        ARBShaderObjects.glCompileShaderARB(shaderID); 
     
        if (ARBShaderObjects.glGetObjectParameteriARB(shaderID,
                ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
            throw new RuntimeException("Shader compilation error!\n" +
                    ARBShaderObjects.glGetInfoLogARB(shaderID, ARBShaderObjects.
                            glGetObjectParameteriARB(shaderID, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB)));


        ARBShaderObjects.glAttachObjectARB(programID, shaderID);
        return this;
    }


    public ShaderProgram compile()
    {
        ARBShaderObjects.glLinkProgramARB(programID);
        return this;
    }

    public void start()
    {
        ARBShaderObjects.glUseProgramObjectARB(programID);
    }

    public void stop()
    {
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    
    public int getUniform(String name) 
    {
        return ARBShaderObjects.glGetUniformLocationARB(programID, name);
    }


    private String readFile(String path)
    {
        try 
        {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(DragonSurvivalMod.MODID, path)).getInputStream(), "UTF-8"));
            String str;
            while ((str = reader.readLine()) != null)
                builder.append(str).append("\n");

            return builder.toString();

        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
}
