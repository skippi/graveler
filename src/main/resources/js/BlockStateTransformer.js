var InsnList = Java.type("org.objectweb.asm.tree.InsnList")
var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode")
var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode")
var Opcodes = Java.type("org.objectweb.asm.Opcodes")
var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

var ALOAD = function (argNo) { return new VarInsnNode(Opcodes.ALOAD, argNo) }
var ICONST_1 = new InsnNode(Opcodes.ICONST_1)
var IRETURN = new InsnNode(Opcodes.IRETURN)

function initializeCoreMod() {
  return {
    'updateNeighbors': {
      'target': {
          'type': 'METHOD',
          'class': 'net.minecraft.block.BlockState',
          'methodName': ASMAPI.mapMethod('func_196946_a'), // BlockState#updateNeighbors
          'methodDesc': '(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;I)V'
      },
      'transformer': function(method) {
          var mixin = new InsnList()
          mixin.add(ALOAD(1))
          mixin.add(ALOAD(2))
          mixin.add(ASMAPI.buildMethodCall(
            "graveler/Mixins",
            "updateNeighbors",
            "(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;)V",
            ASMAPI.MethodType.STATIC))

          method.instructions.insert(mixin)
          return method;
      }
    },
    'onBlockAdded': {
      'target': {
          'type': 'METHOD',
          'class': 'net.minecraft.block.BlockState',
          'methodName': ASMAPI.mapMethod('func_215705_a'), // BlockState#onBlockAdded
          'methodDesc': '(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V'
      },
      'transformer': function(method) {
          var mixin = new InsnList()
          mixin.add(ALOAD(1))
          mixin.add(ALOAD(2))
          mixin.add(ASMAPI.buildMethodCall(
            "graveler/Mixins",
            "onBlockAdded",
            "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V",
            ASMAPI.MethodType.STATIC))

          method.instructions.insert(mixin)
          return method;
      }
    }
  }
}
